package com.yufan.ai.tools;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.yufan.ai.entity.po.Course;
import com.yufan.ai.entity.po.CourseReservation;
import com.yufan.ai.entity.po.School;
import com.yufan.ai.entity.query.CourseQuery;
import com.yufan.ai.service.ICourseReservationService;
import com.yufan.ai.service.ICourseService;
import com.yufan.ai.service.ISchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
@RequiredArgsConstructor
@Component
public class CourseTools {

    private final ICourseService courseService;
    private final ISchoolService schoolService;
    private final ICourseReservationService courseReservationService;

    @Tool(description = "根据条件查询返回课程")
    public List<Course> queryCourse(@ToolParam(description ="查询的条件" ) CourseQuery courseQuery){
        if(courseQuery == null){
            return courseService.list();
        }
        QueryChainWrapper<Course> wrapper =courseService.query()
                .eq(courseQuery.getType()!=null,"type",courseQuery.getType())
                .le(courseQuery.getEdu()!=null,"edu",courseQuery.getEdu());
        if(courseQuery.getSorts()!=null&&courseQuery.getSorts().size()>0){
            for(CourseQuery.Sort sort:courseQuery.getSorts()){
                wrapper.orderBy(true, sort.getAsc(), sort.getField());
            }
        }
        return wrapper.list();
    }

    @Tool(description = "查询所有校区")
    public List<School> queryAllSchool(){
        return schoolService.list();
    }

    @Tool(description = "生成预约单，返回预约单号")
    public Integer saveCourseReservation(
            @ToolParam(description = "预约课程") String course,
            @ToolParam(description = "预约校区") String school,
            @ToolParam(description = "学生姓名") String studentName,
            @ToolParam(description = "联系方式") String contactInfo,
            @ToolParam(description = "备注",required = false) String remark
                                         ){
        CourseReservation courseReservation = new CourseReservation();
        courseReservation.setCourse(course);
        courseReservation.setSchool(school);
        courseReservation.setStudentName(studentName);
        courseReservation.setContactInfo(contactInfo);
        courseReservation.setRemark(remark);
        courseReservationService.save(courseReservation);
        return courseReservation.getId();
    }
}
