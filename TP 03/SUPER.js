// Javascript implementation of above approach
 
// Function to find K shortest path lengths
function findKShortest(edges, n, m, k)
{
 
    // Initialize graph
    var g = Array.from(Array(n + 1), ()=>new Array());
    for (var i = 0; i < m; i++)
    {
     
        // Storing edges
        g[edges[i][0]].push([edges[i][1], edges[i][2]]);
    }
 
    // Vector to store distances
    var dis = Array.from(Array(n+1), ()=> Array(k).fill(1000000000));
 
    // Initialization of priority queue
    var pq = [];
    pq.push([0, 1]);
    dis[1][0] = 0;
 
    // while pq has elements
    while (pq.length != 0)
    {
     
        // Storing the node value
        var u = pq[pq.length-1][1];
 
        // Storing the distance value
        var d = (pq[pq.length-1][0]);
        pq.pop();
        if (dis[u][k - 1] < d)
            continue;
        var v = g[u];
 
        // Traversing the adjacency list
        for (var i = 0; i < v.length; i++) {
            var dest = v[i][0];
            var cost = v[i][1];
 
            // Checking for the cost
            if (d + cost < dis[dest][k - 1]) {
                dis[dest][k - 1] = d + cost;
 
                // Sorting the distances
                dis[dest].sort((a,b)=>a-b);
 
                // Pushing elements to priority queue
                pq.push([(d + cost), dest ]);
                pq.sort();
            }
        }
    }
 
    // Printing K shortest paths
    for (var i = 0; i < k; i++) {
        console.log(dis[n][i] + " ");
    }
}
 
// Driver Code
 
// Given Input
var N = 4, M = 6, K = 1;
var edges = [ [ 1, 2, 1 ], [ 1, 3, 3 ],
              [ 2, 3, 2 ], [ 2, 4, 6 ],
              [ 3, 2, 8 ], [ 3, 4, 1 ] ];
                    
// Function Call
findKShortest(edges, N, M, K);
 
// This code is contributed by rrrtnx.