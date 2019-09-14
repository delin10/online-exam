package nil.ed.onlineexam.controller;

import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.entity.Permission;
import nil.ed.onlineexam.service.IPermissionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam/resource")
public class ResourceController {
    @javax.annotation.Resource
    @Qualifier("permissionService")
    private IPermissionService permissionService;

    @GetMapping(value = "/list/currentUser")
    public Response<List<Permission>> listResourcesOfCurrentUser(@RequestAttribute("user") UserDetails user){
        return permissionService.listPermissionsOfCurrentUser(Integer.valueOf(user.getUsername()));
    }

    @GetMapping(value = "/list/all")
    public Response<List<Permission>> listResources(){
        return permissionService.listAllPermissions();
    }

    @GetMapping(value = "/list/ofRole/{roleId}")
    public Response<List<Permission>> listResources(@PathVariable("roleId") Integer roleId){
        return permissionService.listPermissionsOfRole(roleId);
    }

    @PostMapping(value = "/add")
    public Response<Void> addResource(@RequestBody Permission permission,
                                      @RequestAttribute("user") UserDetails user){
        return permissionService.addPermission(permission, Integer.valueOf(user.getUsername()));
    }
}
