package solutions.desati.twatter.models

import javax.persistence.*

@Entity
class User (
    @Id
    @GeneratedValue
    var id: Long? = null,
    val email: String,
    val name: String,
    val hashedPassword: String
)
