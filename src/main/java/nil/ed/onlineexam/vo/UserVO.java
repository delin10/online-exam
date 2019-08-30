package nil.ed.onlineexam.vo;

import lombok.Data;
import nil.ed.onlineexam.entity.Role;

@Data
public class UserVO {
    private Integer id;

    private String nickName;

    private Role role;

    private Long createTime;
}
