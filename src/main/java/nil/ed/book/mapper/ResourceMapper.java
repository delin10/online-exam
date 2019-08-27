package nil.ed.book.mapper;

import nil.ed.book.entity.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface ResourceMapper {
    int insert(Resource record);

    int insertSelective(Resource record);

    List<Resource> listAllResources();

    Resource getResourceById(@Param("id") Integer id);

    int deleteResourceById(@Param("id") Integer id);

    int updateResource(@Param("resource") Resource resource);
}