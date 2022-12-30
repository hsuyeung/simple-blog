package com.hsuyeung.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hsuyeung.blog.model.entity.FileEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.file.FileInfoVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 文件服务接口
 *
 * @author hsuyeung
 * @date 2022/06/27
 */
public interface IFileService extends IService<FileEntity> {
    /**
     * 上传文件
     *
     * @param file {@link MultipartFile}
     * @return 上传成功后的完整访问路径
     * @throws IOException 文件保存失败时抛出该异常
     */
    String upload(MultipartFile file) throws IOException;

    /**
     * 读取文件
     *
     * @param year     年
     * @param month    月
     * @param day      日
     * @param fileName 文件名
     * @param response {@link HttpServletResponse}
     */
    void readFile(String year, String month, String day, String fileName, HttpServletResponse response)
            throws UnsupportedEncodingException;

    /**
     * 查询文件上传记录分页列表
     *
     * @param url            文件访问路径，全模糊
     * @param startTimestamp 开始时间戳，大于等于
     * @param endTimestamp   结束时间戳，小于等于
     * @param pageNum        页码
     * @param pageSize       每页数量
     * @return 文件上传记录分页列表
     */
    PageVO<FileInfoVO> getFilePage(String url, Integer pageNum, Long startTimestamp, Long endTimestamp, Integer pageSize);
}
