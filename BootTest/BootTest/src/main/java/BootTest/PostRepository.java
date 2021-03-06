package BootTest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.WriteResult;

@Service
public class PostRepository {
	static final Logger logger = LoggerFactory.getLogger(Post.class);
	@Autowired
	MongoTemplate mongoTemplate;

	public List<Post> getAllPost() {
		return (getAllPost(-1));
	}

	public List<Post> getAllPost(int limit) {
		List<Post> result = null;

		if (mongoTemplate.collectionExists(Post.class)) {
			Query query = new Query();
			if (limit > 0)
				query.limit(limit);
			query.with(new Sort(Sort.Direction.DESC, "creation"));
			result = mongoTemplate.find(query, Post.class);
		} else {
			// TODO raise error
			logger.error(Post.class.toString() + " collection not found");
		}
		return (result);
	}

	public Post getPostById(String id) {
		if (mongoTemplate.collectionExists(Post.class)) {
			Query query = new Query(Criteria.where("_id").is(id));
			Post result = mongoTemplate.findOne(query, Post.class);
			return result;
		} else {
			// TODO raise error
			logger.error(Post.class.toString() + " collection not found");
		}
		return (null);
	}

	public void savePost(Post post) {
		mongoTemplate.insert(post);
	}

	public WriteResult updateTitleById(String id, String title) {
		if (mongoTemplate.collectionExists(Post.class)) {
			Query query = new Query(Criteria.where("id").is(id));
			return mongoTemplate.updateFirst(query,
					Update.update("title", title), Post.class);
		} else {
			// TODO raise error
			logger.error(Post.class.toString() + " collection not found");
		}
		return (null);
	}

	public WriteResult updateAuthorById(String id, String author) {
		if (mongoTemplate.collectionExists(Post.class)) {
			Query query = new Query(Criteria.where("id").is(id));
			return mongoTemplate.updateFirst(query,
					Update.update("author", author), Post.class);
		} else {
			// TODO raise error
			logger.error(Post.class.toString() + " collection not found");
		}
		return (null);
	}

	public WriteResult updateContentById(String id, String content) {
		if (mongoTemplate.collectionExists(Post.class)) {
			Query query = new Query(Criteria.where("id").is(id));
			return mongoTemplate.updateFirst(query,
					Update.update("content", content), Post.class);
		} else {
			// TODO raise error
			logger.error(Post.class.toString() + " collection not found");
		}
		return (null);
	}

	public void updatePostById(String id, String title, String author,
			String content) {
		if (mongoTemplate.collectionExists(Post.class)) {
			this.updateTitleById(id, title);
			this.updateAuthorById(id, author);
			this.updateContentById(id, content);
		} else {
			// TODO raise error
			logger.error(Post.class.toString() + " collection not found");
		}
	}

	public WriteResult deletePostById(String id) {
		if (mongoTemplate.collectionExists(Post.class)) {
			Query query = new Query(Criteria.where("id").is(id));
			return (mongoTemplate.remove(query, Post.class));
		} else {
			// TODO raise error
			logger.error(Post.class.toString() + " collection not found");
		}
		return (null);
	}

	public void addCommentByPostId(String id, String comment) {
		if (mongoTemplate.collectionExists(Post.class) && comment !=null && !comment.isEmpty()) {
			Query query = new Query(Criteria.where("_id").is(id));
			mongoTemplate.updateFirst(query,
					new Update().push("comments", comment), Post.class);
		} else {
			// TODO raise error
			logger.error(Post.class.toString() + " collection not found");
		}

	}
}
