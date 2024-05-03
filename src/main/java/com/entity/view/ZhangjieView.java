package com.entity.view;

import org.apache.tools.ant.util.DateUtils;
import com.annotation.ColumnInfo;
import com.entity.ZhangjieEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import com.utils.DateUtil;

/**
* 章节
* 后端返回视图实体辅助类
* （通常后端关联的表或者自定义的字段需要返回使用）
*/
@TableName("zhangjie")
public class ZhangjieView extends ZhangjieEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//当前表

	//级联表 课程
		/**
		* 课程名称
		*/

		@ColumnInfo(comment="课程名称",type="varchar(200)")
		private String kechengName;
		/**
		* 课程编号
		*/

		@ColumnInfo(comment="课程编号",type="varchar(200)")
		private String kechengUuidNumber;
		/**
		* 上课地点
		*/

		@ColumnInfo(comment="上课地点",type="varchar(200)")
		private String kechengAddress;
		/**
		* 课程照片
		*/

		@ColumnInfo(comment="课程照片",type="varchar(200)")
		private String kechengPhoto;
		/**
		* 课程文件
		*/

		@ColumnInfo(comment="课程文件",type="varchar(200)")
		private String kechengFile;
		/**
		* 课程视频
		*/

		@ColumnInfo(comment="课程视频",type="varchar(200)")
		private String kechengVideo;
		/**
		* 课程类型
		*/
		@ColumnInfo(comment="课程类型",type="int(11)")
		private Integer kechengTypes;
			/**
			* 课程类型的值
			*/
			@ColumnInfo(comment="课程类型的字典表值",type="varchar(200)")
			private String kechengValue;
		/**
		* 上课时间
		*/
		@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
		@DateTimeFormat
		@ColumnInfo(comment="上课时间",type="timestamp")
		private Date kechengTime;
		/**
		* 课程介绍
		*/

		@ColumnInfo(comment="课程介绍",type="longtext")
		private String kechengContent;
		/**
		* 逻辑删除
		*/

		@ColumnInfo(comment="逻辑删除",type="int(11)")
		private Integer kechengDelete;



	public ZhangjieView() {

	}

	public ZhangjieView(ZhangjieEntity zhangjieEntity) {
		try {
			BeanUtils.copyProperties(this, zhangjieEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}





	//级联表的get和set 课程

		/**
		* 获取： 课程名称
		*/
		public String getKechengName() {
			return kechengName;
		}
		/**
		* 设置： 课程名称
		*/
		public void setKechengName(String kechengName) {
			this.kechengName = kechengName;
		}

		/**
		* 获取： 课程编号
		*/
		public String getKechengUuidNumber() {
			return kechengUuidNumber;
		}
		/**
		* 设置： 课程编号
		*/
		public void setKechengUuidNumber(String kechengUuidNumber) {
			this.kechengUuidNumber = kechengUuidNumber;
		}

		/**
		* 获取： 上课地点
		*/
		public String getKechengAddress() {
			return kechengAddress;
		}
		/**
		* 设置： 上课地点
		*/
		public void setKechengAddress(String kechengAddress) {
			this.kechengAddress = kechengAddress;
		}

		/**
		* 获取： 课程照片
		*/
		public String getKechengPhoto() {
			return kechengPhoto;
		}
		/**
		* 设置： 课程照片
		*/
		public void setKechengPhoto(String kechengPhoto) {
			this.kechengPhoto = kechengPhoto;
		}

		/**
		* 获取： 课程文件
		*/
		public String getKechengFile() {
			return kechengFile;
		}
		/**
		* 设置： 课程文件
		*/
		public void setKechengFile(String kechengFile) {
			this.kechengFile = kechengFile;
		}

		/**
		* 获取： 课程视频
		*/
		public String getKechengVideo() {
			return kechengVideo;
		}
		/**
		* 设置： 课程视频
		*/
		public void setKechengVideo(String kechengVideo) {
			this.kechengVideo = kechengVideo;
		}
		/**
		* 获取： 课程类型
		*/
		public Integer getKechengTypes() {
			return kechengTypes;
		}
		/**
		* 设置： 课程类型
		*/
		public void setKechengTypes(Integer kechengTypes) {
			this.kechengTypes = kechengTypes;
		}


			/**
			* 获取： 课程类型的值
			*/
			public String getKechengValue() {
				return kechengValue;
			}
			/**
			* 设置： 课程类型的值
			*/
			public void setKechengValue(String kechengValue) {
				this.kechengValue = kechengValue;
			}

		/**
		* 获取： 上课时间
		*/
		public Date getKechengTime() {
			return kechengTime;
		}
		/**
		* 设置： 上课时间
		*/
		public void setKechengTime(Date kechengTime) {
			this.kechengTime = kechengTime;
		}

		/**
		* 获取： 课程介绍
		*/
		public String getKechengContent() {
			return kechengContent;
		}
		/**
		* 设置： 课程介绍
		*/
		public void setKechengContent(String kechengContent) {
			this.kechengContent = kechengContent;
		}

		/**
		* 获取： 逻辑删除
		*/
		public Integer getKechengDelete() {
			return kechengDelete;
		}
		/**
		* 设置： 逻辑删除
		*/
		public void setKechengDelete(Integer kechengDelete) {
			this.kechengDelete = kechengDelete;
		}


	@Override
	public String toString() {
		return "ZhangjieView{" +
			", kechengName=" + kechengName +
			", kechengUuidNumber=" + kechengUuidNumber +
			", kechengAddress=" + kechengAddress +
			", kechengPhoto=" + kechengPhoto +
			", kechengFile=" + kechengFile +
			", kechengVideo=" + kechengVideo +
			", kechengTime=" + DateUtil.convertString(kechengTime,"yyyy-MM-dd") +
			", kechengContent=" + kechengContent +
			", kechengDelete=" + kechengDelete +
			"} " + super.toString();
	}
}
