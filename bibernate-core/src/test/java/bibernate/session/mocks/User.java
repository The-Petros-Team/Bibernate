package bibernate.session.mocks;

import com.bobocode.petros.bibernate.annotations.Entity;
import com.bobocode.petros.bibernate.annotations.Id;
import com.bobocode.petros.bibernate.annotations.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    private Long id;
    private String name;
    private String email;
}
