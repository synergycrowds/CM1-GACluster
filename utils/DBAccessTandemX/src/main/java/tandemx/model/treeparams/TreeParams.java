package tandemx.model.treeparams;

import javax.persistence.*;

@Entity
@Table(name = "TREE")
public class TreeParams {
    @Id
    @Column(name = "tree_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer treeId;

    public TreeParams() {
    }

    public Integer getTreeId() {
        return treeId;
    }

    public void setTreeId(Integer treeId) {
        this.treeId = treeId;
    }
}
