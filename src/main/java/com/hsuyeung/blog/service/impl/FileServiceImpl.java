package com.hsuyeung.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsuyeung.blog.cache.lfu.LFUCache;
import com.hsuyeung.blog.exception.SystemInternalException;
import com.hsuyeung.blog.mapper.FileMapper;
import com.hsuyeung.blog.model.entity.FileEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.file.FileInfoVO;
import com.hsuyeung.blog.service.IFileService;
import com.hsuyeung.blog.service.IUserService;
import com.hsuyeung.blog.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hsuyeung.blog.constant.CommonConstants.ONE_KB;
import static com.hsuyeung.blog.constant.DateFormatConstants.FORMAT_YEAR_TO_SECOND;
import static org.apache.commons.codec.CharEncoding.UTF_8;

/**
 * 文件服务实现类
 *
 * @author hsuyeung
 * @date 2022/06/27
 */
@Slf4j
@Service("fileService")
public class FileServiceImpl extends ServiceImpl<FileMapper, FileEntity> implements IFileService {
    private static final String PERCENT_SIGN = "%";
    /**
     * 合法的图片后缀名
     */
    private static final List<String> VALID_IMG_SUFFIX = Arrays.asList(
            "jpg", "jpeg", "png", "gif", "svg", "webp", "bmp"
    );
    /**
     * 文件缓存
     */
    private static final LFUCache LFU_CACHE = new LFUCache(128);
    /**
     * 文件上传路径
     */
    @Value("${file.upload-path}")
    private String fileUploadPath;
    /**
     * 文件夹命名的日期格式
     */
    @Value("${file.dir-fmt}")
    private String fileDirFmt;
    /**
     * 文件上传路径前缀
     */
    @Value("${file.domain}")
    private String domain;

    @Resource
    private IUserService userService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String upload(MultipartFile file) throws IOException {
        log.info("开始上传文件：{}", file);
        String dir = DateUtil.formatLocalDateTime(LocalDateTime.now(), fileDirFmt);
        File folder = new File(String.format("%s%s%s", fileUploadPath, fileUploadPath.endsWith("/") ? "" : "/", dir));
        if (!folder.isDirectory()) {
            AssertUtil.isTrue(folder.mkdirs(), "创建文件夹失败");
        }
        String fileName = file.getOriginalFilename();
        BizAssertUtil.hasLength(fileName, "文件名不能为空");
        if (fileName.contains(PERCENT_SIGN)) {
            fileName = URLDecoder.decode(fileName, UTF_8);
        }
        BizAssertUtil.hasLength(FileUtil.getSuffix(fileName), "文件名后缀不能为空");
        File saveFile = new File(folder, fileName);
        BizAssertUtil.isTrue(!saveFile.exists(), String.format("文件名重复：%s", fileName));
        // 文件保存
        file.transferTo(saveFile);
        log.info("上传文件成功，磁盘路径：{}/{}", folder.getAbsolutePath(), fileName);
        // 返回上传文件的访问路径，在文件路径的最前面加上读取文件的接口的 api 路径前缀
        // 这样前端请求文件时就会路由到读取文件的接口上，然后根据文件的路径来从服务器本地获取文件流返回给前端
        String filePath = String.format("%s/file/%s/%s", domain, dir, fileName);
        save(FileEntity.builder().url(filePath).build());
        log.info("文件上传记录保存成功");
        return filePath;
    }

    @Override
    public void readFile(String year, String month, String day, String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        String filePath = String.format("%s%s%s/%s/%s/%s",
                fileUploadPath,
                fileUploadPath.endsWith("/") ? "" : "/",
                year, month, day, fileName);
        if (filePath.contains(PERCENT_SIGN)) {
            filePath = URLDecoder.decode(filePath, UTF_8);
        }
        byte[] fileData = LFU_CACHE.get(filePath);
        try (
                FileInputStream fis = new FileInputStream(filePath);
                ServletOutputStream sos = response.getOutputStream()
        ) {
            if (fileData.length == 0) {
                // 无缓存，则从服务器读取文件
                log.info("从服务器读取文件：{}", filePath);
                int available = fis.available();
                fileData = new byte[available];
                int offset = 0;
                int len;
                while (offset < available && (len = fis.read(fileData, offset, Math.min(10 * ONE_KB, available - offset))) != -1) {
                    offset += len;
                }
                // 放入缓存
                LFU_CACHE.put(filePath, fileData);
            } else {
                // 有缓存，则直接返回缓存数据
                log.info("从缓存获取文件：{}", filePath);
            }
            sos.write(fileData);
            sos.flush();
            this.setContentType(response, FileUtil.getSuffix(filePath));
            log.info("获取文件成功：{}", filePath);
        } catch (Exception e) {
            log.info("获取文件失败：{}", filePath);
            throw new SystemInternalException("获取文件失败：" + e.getMessage(), e);
        }
    }

    @Override
    public PageVO<FileInfoVO> getFilePage(String url, Integer pageNum, Long startTimestamp, Long endTimestamp, Integer pageSize) {
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (Objects.nonNull(startTimestamp)) {
            startTime = DateUtil.fromLongToJava8LocalDate(startTimestamp);
        }
        if (Objects.nonNull(endTimestamp)) {
            endTime = DateUtil.fromLongToJava8LocalDate(endTimestamp);
        }
        Page<FileEntity> entityPage = lambdaQuery()
                .select(FileEntity::getId, FileEntity::getUrl, FileEntity::getCreateTime, FileEntity::getCreateBy)
                .like(StringUtils.hasLength(url), FileEntity::getUrl, url)
                .ge(Objects.nonNull(startTime), FileEntity::getCreateTime, startTime)
                .le(Objects.nonNull(endTime), FileEntity::getCreateTime, endTime)
                .orderByDesc(FileEntity::getCreateTime)
                .page(new Page<>(pageNum, pageSize));
        List<FileEntity> entityList = entityPage.getRecords();
        if (CollectionUtils.isEmpty(entityList)) {
            return new PageVO<>(0L, Collections.emptyList());
        }
        List<FileInfoVO> voList = entityList.stream().map(entity -> {
            FileInfoVO vo = ConvertUtil.convert(entity, FileInfoVO.class);
            vo.setCreateTime(DateUtil.formatLocalDateTime(entity.getCreateTime(), FORMAT_YEAR_TO_SECOND));
            String createBy = entity.getCreateBy();
            vo.setCreateBy(userService.getUsernameById(StringUtils.hasLength(createBy) ? Long.parseLong(createBy) : null));
            return vo;
        }).collect(Collectors.toList());
        return new PageVO<>(entityPage.getTotal(), voList);
    }

    // --------------------------------------------- PRIVATE METHOD REGION ---------------------------------------------

    /**
     * 设置响应的 content-type 属性值
     *
     * @param response {@link HttpServletResponse}
     * @param suffix   文件后缀名
     */
    private void setContentType(HttpServletResponse response, String suffix) {
        if (VALID_IMG_SUFFIX.contains(suffix)) {
            response.setContentType("image/" + suffix);
        } else {
            // 自动判断下载文件类型
            response.setContentType("multipart/form-data");
        }
    }
}
