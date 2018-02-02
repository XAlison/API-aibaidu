package org.singleton.aibaidu.controller;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.List;

import org.singleton.aibaidu.config.Config;
import org.singleton.aibaidu.config.ConfigURL;
import org.singleton.aibaidu.core.AccessToken;
import org.singleton.aibaidu.entity.Result;
import org.singleton.aibaidu.utils.Base64Util;
import org.singleton.aibaidu.utils.FileUtil;
import org.singleton.aibaidu.utils.GsonUtils;
import org.singleton.aibaidu.utils.HttpUtil;
import org.singleton.aibaidu.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/ai")
public class RecognitionController {
	
	@Autowired
	public Config orc;
	

	/**
	 * 文件上传解析
	 * @methodsDescription:
	 * @methodName: rec
	 * @param file
	 * @return
	 * @author: singleton-zw
	 * @return: R
	 */
	@RequestMapping(value="/recognLocal" ,method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public R rec(@RequestParam("file") MultipartFile file){
		if (file.isEmpty()) {
			return R.ok().put("orc","文件不能为空");
		}
		String path = orc.getUpladfile()+file.getOriginalFilename();
		String relst = "";
		try {
            // 这里只是简单例子，文件直接输出到项目路径下。
            // 实际项目中，文件需要输出到指定位置，需要在增加代码处理。
            // 还有关于文件格式限制、文件大小限制，详见：中配置。
            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream(path));
            out.write(file.getBytes());
            out.flush();
            out.close();
            
            byte[] imgData = FileUtil.readFileByBytes(path);
            String imgStr = Base64Util.encode(imgData);
            relst = URLEncoder.encode("image", "UTF-8") + "="    + URLEncoder.encode(imgStr, "UTF-8");
            
          /**
          * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
          */
         String accessToken = AccessToken.getAuth(orc.getClientId(),orc.getClientSecret());
         String result = HttpUtil.post(ConfigURL.OrcUrl, accessToken, relst);
         Result fromJson = GsonUtils.fromJson(result, Result.class); 
         List<Result.Word> data = fromJson.getWords_result();
         String w = "";
         for (Result.Word word : data) {
				w += word.getWords()+"<br>";
			}
         return R.ok().put("orc", w);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return R.ok().put("orc", "失败");
	}
	
	/**
	 * 使用网络图片
	 * @methodsDescription:
	 * @methodName: rec2
	 * @param file
	 * @return
	 * @author: singleton-zw
	 * @return: R
	 */
	@RequestMapping(value="/recognNet" ,method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public R rec2(@RequestParam("urlNet") String urlNet){
		if (urlNet.isEmpty()) {
			return R.ok().put("orc","url不能为空");
		}
		String relst = "";
		try {
            relst = URLEncoder.encode("url", "UTF-8") + "="   + URLEncoder.encode(urlNet, "UTF-8");
            
          /**
          * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
          */
         String accessToken = AccessToken.getAuth(orc.getClientId(),orc.getClientSecret());
         String result = HttpUtil.post(ConfigURL.OrcUrl, accessToken, relst);
         Result fromJson = GsonUtils.fromJson(result, Result.class); 
         List<Result.Word> data = fromJson.getWords_result();
         String w = "";
         for (Result.Word word : data) {
				w += word.getWords()+"<br>";
			}
         return R.ok().put("orc", w);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return R.ok().put("orc", "失败");
	}
}
