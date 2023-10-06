package org.jetbrains.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildDto implements Serializable {

    private static final long serialVersionUID = 1370628614219788604L;

    private String repositoryUrl;
    private String branch;
    private boolean scheduled;
}
