package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


public class PostRepository {

    private final Map<Long, Post> listPosts = new ConcurrentHashMap<>();
    private final AtomicLong postID = new AtomicLong();

    public List<Post> all() {
        return new ArrayList<>(listPosts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(listPosts.get(id));
    }

    //    Если от клиента приходит пост с id=0, значит, это создание нового поста. Вы сохраняете его в списке и присваиваете ему новый id.
    //    Если от клиента приходит пост с id !=0, значит, это сохранение (обновление) существующего поста. Вы ищете его в списке по id и обновляете.

    public Post save(Post post) {
        if (post.getId() == 0) {
            listPosts.put(postID.getAndIncrement(), new Post(post.getId(), post.getContent()));
            return post;
        } else {
            Post postId = listPosts.get(post.getId());
            if (listPosts.containsKey(post.getId())) {
                final long id = listPosts.get(post.getId()).getId();
                listPosts.put(id, new Post(post.getId(), post.getContent()));
                return post;
            } else if (postId == null) {
                throw new NotFoundException("Поста с таким id нету");
            }
        }
        return post;
    }

    public void removeById(long id) {
        listPosts.remove(id);
    }
}
