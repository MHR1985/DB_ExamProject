package dk.thesocialnetwork.repository;

import dk.thesocialnetwork.model.Image;
import dk.thesocialnetwork.model.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query(value = "SELECT i.* FROM image i " +
            "left join account_images ai on ai.images_id = i.id " +
            "left join account a on a.id = ai.user_id " +
            "WHERE i.title = :imageTitle and a.username = :username", nativeQuery = true)
    Image findImageWithUsername(@Param("username") String username,@Param("imageTitle") String imageTitle);
}
