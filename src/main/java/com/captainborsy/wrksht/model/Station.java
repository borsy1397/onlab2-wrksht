package com.captainborsy.wrksht.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "station")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Station {

    @Id
    @Column(length = 36)
    @GeneratedValue(generator = "uuid_generator")
    @GenericGenerator(name = "uuid_generator", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "station", fetch = FetchType.LAZY)
    private List<Workflow> workflows;

    @OneToOne( fetch = FetchType.LAZY)
    private User loggedInUser;

    @ColumnDefault("false")
    private boolean isDeleted;

    @ColumnDefault("false")
    private boolean isSet;

    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
