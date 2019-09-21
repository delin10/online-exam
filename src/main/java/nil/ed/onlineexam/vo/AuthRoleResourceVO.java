package nil.ed.onlineexam.vo;

import lombok.Data;

import java.util.Set;

@Data
public class AuthRoleResourceVO {
    private Integer roleId;

    private Set<Integer> resources;
}
