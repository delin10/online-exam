package nil.ed.book.mapper;

import nil.ed.book.entity.Order;

public interface OrderMapper {
    int insert(Order record);

    int insertSelective(Order record);
}