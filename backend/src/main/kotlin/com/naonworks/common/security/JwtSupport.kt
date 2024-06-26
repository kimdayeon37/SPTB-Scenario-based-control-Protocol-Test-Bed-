package com.naonworks.common.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

class BearerToken(
    val value: String,
    val ipAddress: String?,
    val userAgent: String?
) : AbstractAuthenticationToken(AuthorityUtils.NO_AUTHORITIES) {
    override fun getCredentials(): Any = value

    override fun getPrincipal(): Any = value
}

@Component
class JwtSupport {
    private val key =
        Keys.hmacShaKeyFor("aaaaaaaaaabbbbbbbbbbccccccccccdd12321312312sdadasd12312312312313asdsrf1245wevf243a".toByteArray())
    private val parser = Jwts.parserBuilder().setSigningKey(key).build()
    fun generate(username: String): BearerToken {
        val builder = Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plus(15, ChronoUnit.MINUTES)))
            .signWith(key)

        return BearerToken(builder.compact(),"","")
    }

    fun getId(token: BearerToken): String {
        return parser.parseClaimsJws(token.value).body.subject
    }

    fun isValid(token: BearerToken, user: UserDetails?): Boolean {
        val claims = parser.parseClaimsJws(token.value).body
        val unexpired = claims.expiration.after(Date.from(Instant.now()))

        return unexpired && (claims.subject == user?.username)
    }

    fun isExpired(token: BearerToken): Boolean =
        try {
            parser.parseClaimsJws(token.value).body.expiration.before(Date.from(Instant.now()))
        } catch (_: Exception) {
            false
        }

}