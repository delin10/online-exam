package nil.ed.book.mapper;

import nil.ed.book.entity.BookComment;

public interface BookCommentMapper {
    int insert(BookComment record);

    int insertSelective(BookComment record);
}