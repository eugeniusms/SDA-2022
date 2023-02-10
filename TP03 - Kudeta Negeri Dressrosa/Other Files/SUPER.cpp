#include <bits/stdc++.h>
using namespace std;

// https://www.geeksforgeeks.org/ordered-set-gnu-c-pbds/
#define fastio ios_base::sync_with_stdio(0);cin.tie(0);cout.tie(0);
#define rep(i,s,e) for(int i=s ; i < e ; i++)
#define rrep(i,s,e) for(int i=s ; i > e ; i--)
#define srep(i,s,e,j) for(int i=s ; i < e ; i+=j)
#define tr(i,x) for(auto i : x)
#define vinp(a) for(int i=0 ; i<a.size() ; i++)cin>>a[i]
#define ainp(a,n) for(int i=0; i<n; i++)cin>>a[i]
#define int long long
#define vi vector<int>
#define vvi vector<vector<int>>
#define vs vector<string>
#define vb vector<bool>
#define vpi vector<pii>
#define maxpqi priority_queue<int>
#define minpqi priority_queue <int, vector<int>, greater<int> >
#define mem0(x) memset(x, 0, sizeof(x))
#define mem1(x) memset(x, -1, sizeof(x))
#define pii pair<int,int> 
#define F first
#define S second
#define mk make_pair
#define pb push_back
#define pf push_front
#define endl '\n'
#define gcd(a,b) __gcd(a,b)
#define clr(x) memset(x,0,sizeof(x))
#define lb lower_bound
#define ub upper_bound
#define npos string::npos
#define all(x) x.begin(),x.end()
#define sayyes cout << "YES" << endl
#define sayno cout << "NO" << endl

struct Node{
    int v, k, d;
    Node(){
        v = k = d = 0;
    }
    Node(int _v, int _k, int _d){
        v = _v,k = _k,d = _d;
    }
};
typedef struct Node Node;

class Compare{
public:
    bool operator()(const Node& a, const Node& b){
        // min priority queue according to dist
        return  a.d > b.d;
    }
};

vector<vpi>adj;
vvi dp;
int n, K;

void dijkstra(int src){
    dp[src][0] = 0; // distance from src to src after using 0 coupon is 0
    priority_queue<Node, vector<Node>, Compare>pq;
    pq.push(Node(src, 0, 0));
    
    Node tp;
    int from, k, dist;
    int to, edgew;
    while(!pq.empty()){
        tp = pq.top(); pq.pop();
        from = tp.v, k = tp.k, dist = tp.d;
        if(dp[from][k] < dist) continue;

        for(auto p: adj[from]){
            to = p.F, edgew = p.S;
            if(dist + edgew < dp[to][k]){
                dp[to][k] = dist + edgew;
                pq.push(Node(to, k, dp[to][k]));
            }   


            if(k+1 <= K && dist < dp[to][k+1]){
                dp[to][k+1] = dist;
                pq.push(Node(to, k+1, dp[to][k+1]));
            }
        }
    }
}

void solve(){
    int m; cin >> n >> m >> K;
    // K = 1; // max number of edges on which discount can be used.


    adj.assign(n+1, vpi());
    int u, v, w;
    rep(i, 0, m){
        cin >> u >> v >> w;
        adj[u].pb({v, w});
    }

    dp.assign(n+1, vi(K+1, LONG_MAX));
    // ```dp[i][k] denote minimum distance from 1 to i after using discount coupon on k edges```

    dijkstra(1);
    
    int ans = LONG_MAX;
    rep(j, 0, K) ans = min(ans, dp[n][j]);

    cout << ans << endl;
}

int32_t main(){
    fastio
    // freopen("input.txt","r",stdin);
    // freopen("output.txt","w",stdout);
    int tt = 1;
    // cin >> tt;
    while(tt--){
        solve();
        // cout << solve() << endl;
        // cout << (solve() ? "yes" : "no") << endl;
    }
    return 0;
}