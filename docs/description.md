[< CM1-GACluster](../)
[<< Infrastructure](../../infrastructure/)


## Overview

This method is based on a Genetic Algorithm implementation. Its evolutionary approach aims at
clusterizing the set of items (i.e. digital currencies) into a number of clusters based on their price evolution in time. A key feature of the method is to filter the items with similar trajectories over time (included in clusters).

## Data

The default service runs on historical price data from the same period of time for all analyzed digital currencies.

## Performance

### Implementation approaches

#### 1. MapReduce

#### 2. Isles

![isles](images/isles-orientedGraph.png)

![isles](images/isles-cellularTopology.png)
