package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.entity.Resource;

public interface ResourceMapper {
    int insert(Resource record);

    int insertSelective(Resource record);
}