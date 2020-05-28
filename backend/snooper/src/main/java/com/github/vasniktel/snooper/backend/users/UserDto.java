package com.github.vasniktel.snooper.backend.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class UserDto {
  @Id
  @Column(name = "id")
  @JsonProperty
  @NonNull
  private String id;

  @Column(name = "name")
  @JsonProperty
  @NonNull
  private String name;

  @Column(name = "photo_url")
  @JsonProperty
  private String photoUrl;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UserDto userDto = (UserDto) o;

    return id.equals(userDto.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
