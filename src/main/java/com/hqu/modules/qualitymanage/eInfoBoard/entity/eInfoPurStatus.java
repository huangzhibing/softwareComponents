package com.hqu.modules.qualitymanage.eInfoBoard.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.core.persistence.DataEntity;

import java.util.Date;

/**
 * 质检电子板Entity
 * @author zz
 * @version 2018-11-11
 */
public class eInfoPurStatus extends DataEntity<eInfoPurStatus> {
    private static final long serialVersionUID = 1L;
    private String reportId;        //检验单编号
    private String objName;         //检验对象名称
    private String checkprj;		// 测试项目
    private String checkDuration;   //检测时长
    private String isQuality;     //检验结果
    private Date startDate;     //开始时间

    private Date startEndDate;//开始时间搜索条件 by yxb
    private Date startBeginDate;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getCheckprj() {
        return checkprj;
    }

    public void setCheckprj(String checkprj) {
        this.checkprj = checkprj;
    }

    public String getCheckDuration() {
        if(checkDuration == null){
            return "";
        }else {
            Long sec = Long.parseLong(checkDuration);
            Long days = sec / 86400;            //转换天数
            sec = sec % 86400;            //剩余秒数
            Long hours = sec / 3600;            //转换小时
            sec = sec % 3600;                //剩余秒数
            Long minutes = sec / 60;            //转换分钟
            sec = sec % 60;                //剩余秒数
            if (days > 0) {
                return days + "天" + hours + "小时" + minutes + "分" + sec + "秒";
            } else {
                return hours + "小时" + minutes + "分" + sec + "秒";
            }
        }
    }

    public void setCheckDuration(String checkDuration) {
        this.checkDuration = checkDuration;
    }

    public String getIsQuality() {
        if(isQuality != null){
            if(isQuality.equals("t")){
                return "质检合格";
            }else if(isQuality.equals("f")){
                return "质检不合格";
            }
        }
        return isQuality;
    }

    public void setIsQuality(String isQuality) {
        this.isQuality = isQuality;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartEndDate() {
        return startEndDate;
    }

    public void setStartEndDate(Date startEndDate) {
        this.startEndDate = startEndDate;
    }

    public Date getStartBeginDate() {
        return startBeginDate;
    }

    public void setStartBeginDate(Date startBeginDate) {
        this.startBeginDate = startBeginDate;
    }
}

