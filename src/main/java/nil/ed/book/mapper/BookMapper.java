package nil.ed.book.mapper;

import nil.ed.book.entity.Book;

public interface BookMapper {
    int insert(Book record);

    int insertSelective(Book record);
}