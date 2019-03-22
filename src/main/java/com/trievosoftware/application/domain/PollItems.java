package com.trievosoftware.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A PollItems.
 */
@Entity
@Table(name = "poll_items")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PollItems implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "votes")
    private Integer votes;

    @Column(name = "reaction")
    private String reaction;

    @ManyToOne
    @JsonIgnoreProperties("pollitems")
    private Poll poll;

    public PollItems() {
    }

    public PollItems(@NotNull String itemName, Poll poll) {
        this.itemName = itemName;
        this.poll = poll;
        this.votes = 0;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public PollItems itemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public Integer getVotes() {
        return votes;
    }

    public PollItems votes(Integer votes) {
        this.votes = votes;
        return this;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Poll getPoll() {
        return poll;
    }

    public PollItems poll(Poll poll) {
        this.poll = poll;
        return this;
    }

    public PollItems reaction(String reaction)
    {
        this.reaction = reaction;
        return this;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PollItems pollItems = (PollItems) o;
        if (pollItems.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pollItems.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PollItems{" +
            "id=" + getId() +
            ", itemName='" + getItemName() + "'" +
            ", votes=" + getVotes() +
            "}";
    }
}
