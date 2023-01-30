package com.noti.noti.studenthomework.application.port.out;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OutHomeworkOfGivenDate {
  private Long lessonId;
  private String lessonName;
  private LocalTime startTimeOfLesson;
  private LocalTime endTimeOfLesson;
  private List<OutHomeworkOfGivenDate.HomeworkDto> homeworks = new ArrayList<>();

  @Getter
  @NoArgsConstructor
  public static class HomeworkDto {

    private Long homeworkId;
    private String homeworkContent;
    private Long studentCnt;
    private Long completeCnt;

    public HomeworkDto(Long homeworkId, String homeworkContent, Long studentCnt,
        Long completeCnt) {
      this.homeworkId = homeworkId;
      this.homeworkContent = homeworkContent;
      this.studentCnt = studentCnt;
      this.completeCnt = completeCnt;
    }
  }

}
