package net.shinc.service.edu.video;

import java.util.List;

import net.shinc.orm.mybatis.bean.VideoPastpaper;
import net.shinc.orm.mybatis.bean.VideoSelf;

/**
 * @ClassName: VideoSelfService
 * @Description: 自编题服务接口
 * @author hushichong
 * @date 2015年7月31日 下午5:22:01
 */
public interface VideoSelfService {

	public void deleteVideoSelfById(Integer id);

	public Integer insertVideoSelf(VideoSelf videoSelf);

	public void updateVideoSelf(VideoSelf videoSelf);

	public VideoSelf getByVideoSelfById(Integer id);

	public List<VideoSelf> getVideoSelfList(VideoSelf videoSelf);

	public Integer getVideoSelfListCount(VideoSelf videoSelf);

	/**
	 * @Title: getVideoSelf
	 * @Description: 根据对象参数得到单个实例
	 * @param videoSelf
	 * @return videoSelf
	 */
	public VideoSelf getVideoSelf(VideoSelf videoSelf);

	/**
	 * @Title: getVideoSelfAndRelevantInfoList
	 * @Description: 得到自编题以及其相关信息列表
	 * @param videoPastpaper
	 * @return List<VideoPastpaper>
	 */
	public List<VideoSelf> getVideoSelfAndRelevantInfoList(VideoSelf videoSelf);

	/**
	 * @Title: getVideoSelfAndRelevantInfoListCount
	 * @Description: 得到自编题以及其相关信息列表总条数
	 * @param videoPastpaper
	 * @return List<VideoPastpaper>
	 */
	public List<VideoSelf> getVideoSelfAndRelevantInfoListCount(VideoSelf videoSelf);
}