@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int userId;

    public String username;
    public String email;
    public String password;
}
