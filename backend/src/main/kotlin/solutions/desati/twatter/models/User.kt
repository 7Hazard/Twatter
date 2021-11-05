package solutions.desati.twatter.models

import javax.persistence.*

@Entity
class User (
    @Id
    @GeneratedValue
    var id: Long? = null,
    var email: String,
    var username: String,
    var name: String,
    var hashedPassword: String
)
