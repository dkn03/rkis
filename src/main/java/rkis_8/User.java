package rkis_8;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class User implements UserDetails {

    private long id;
    private String username;
    private String password;
    private ArrayList<String> roles = new ArrayList<>();

    public User(){}
    public User(String login, String password, ArrayList<String> roles){
        this.username = login;
        this.password = password;
        this.roles.addAll(roles);
    }

    public User(String login, String password, String role){
        this.username = login;
        this.password = password;
        this.roles.add(role);
    }

    @Override
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String role : this.getRoles()){
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;

    }

    public ArrayList<String> getRoles(){
        return this.roles;
    }

    public void addRole(String newRole){
        this.roles.add(newRole);
    }

    public void deleteRole(String role){
        this.roles.remove(role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public String toString() {
        return "User{" +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles='" + roles + '\'' +
                '}';
    }
}
