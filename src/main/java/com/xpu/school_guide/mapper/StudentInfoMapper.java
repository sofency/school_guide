package com.xpu.school_guide.mapper;

import com.xpu.school_guide.pojo.StudentInfo;
import com.xpu.school_guide.pojo.StudentInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

@Component
public interface StudentInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_info
     *
     * @mbg.generated Tue Jun 30 23:23:59 CST 2020
     */
    long countByExample(StudentInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_info
     *
     * @mbg.generated Tue Jun 30 23:23:59 CST 2020
     */
    int deleteByExample(StudentInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_info
     *
     * @mbg.generated Tue Jun 30 23:23:59 CST 2020
     */
    int deleteByPrimaryKey(String openId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_info
     *
     * @mbg.generated Tue Jun 30 23:23:59 CST 2020
     */
    int insert(StudentInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_info
     *
     * @mbg.generated Tue Jun 30 23:23:59 CST 2020
     */
    int insertSelective(StudentInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_info
     *
     * @mbg.generated Tue Jun 30 23:23:59 CST 2020
     */
    List<StudentInfo> selectByExampleWithRowbounds(StudentInfoExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_info
     *
     * @mbg.generated Tue Jun 30 23:23:59 CST 2020
     */
    List<StudentInfo> selectByExample(StudentInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_info
     *
     * @mbg.generated Tue Jun 30 23:23:59 CST 2020
     */
    StudentInfo selectByPrimaryKey(String openId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_info
     *
     * @mbg.generated Tue Jun 30 23:23:59 CST 2020
     */
    int updateByExampleSelective(@Param("record") StudentInfo record, @Param("example") StudentInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_info
     *
     * @mbg.generated Tue Jun 30 23:23:59 CST 2020
     */
    int updateByExample(@Param("record") StudentInfo record, @Param("example") StudentInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_info
     *
     * @mbg.generated Tue Jun 30 23:23:59 CST 2020
     */
    int updateByPrimaryKeySelective(StudentInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student_info
     *
     * @mbg.generated Tue Jun 30 23:23:59 CST 2020
     */
    int updateByPrimaryKey(StudentInfo record);
}