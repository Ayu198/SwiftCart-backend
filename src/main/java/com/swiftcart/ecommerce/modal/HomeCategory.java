package com.swiftcart.ecommerce.modal;

import com.swiftcart.ecommerce.domain.HomeCategorySection;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"category_id", "section"}
                )
        }
)
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class HomeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String image;
    private String categoryId;
    private HomeCategorySection section;

}
