package ru.levap.cms.persistance.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class FolderParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @NotNull
    @Getter @Setter
    private String name;

    @NotNull
    @Column(length = 4096)
    @Getter @Setter
    private String value;

    @NotNull
    @Getter @Setter
    private Long createDate;
    
    @Getter @Setter
    private Long updateDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "folderId")
    @Getter @Setter
    private Folder folder;
}
