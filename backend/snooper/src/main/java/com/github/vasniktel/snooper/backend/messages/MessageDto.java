package com.github.vasniktel.snooper.backend.messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.vasniktel.snooper.backend.users.UserDto;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messages")
public class MessageDto {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  @JsonProperty
  private int id;

  @Column(name = "latitude")
  @JsonProperty
  private double latitude;

  @Column(name = "longitude")
  @JsonProperty
  private double longitude;

  @Column(name = "address")
  @NonNull
  @JsonProperty
  private String address;

  @Column(name = "description")
  @Nullable
  @JsonProperty
  private String description;

  @ManyToOne
  @NonNull
  @JsonProperty
  private UserDto owner;

  @Column(name = "date")
  @JsonProperty
  @NonNull
  private Date date;

  @Column(name = "likes")
  @JsonProperty
  private int likes;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  @Nullable
  public String getAddress() {
    return address;
  }

  public void setAddress(@Nullable String address) {
    this.address = address;
  }

  @Nullable
  public String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public int getLikes() {
    return likes;
  }

  public void setLikes(int likes) {
    this.likes = likes;
  }

  @NonNull
  public UserDto getOwner() {
    return owner;
  }

  public void setOwner(@NonNull UserDto owner) {
    this.owner = owner;
  }
}
