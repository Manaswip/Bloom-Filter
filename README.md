<html>
<body>
<h2> Bloom filter </h2>
<p>
A bloom filter is space efficient probabilistic data structure that is used to check if the element is member of a dataset or not. False positive matches are possible, but false negatives are not possible thus a Bloom filter has a 100% recall rate. In other words, a query returns either "possibly in set" or "definitely not in set".

<h3> Importance of bloom filter </h3>
<p>
 A Bloom Filter is designed and used to compute joins on 2 relations of size 10^6 bytes. Suppose Relation1.txt is on server-1 and Relation2.txt is on server-2 each having size of 2*10^6 bytes and a join has to be computed between Relation1.txt and Relation2.txt. Let the size of the join is 2*10^5. 
 <h4>Joining relations without Bloom Filter</h4>
 If entire dataset is passed from server-1 to server-2 then data of size 2*10^6*8*(length of each string) bytes is sent which is 16MB
 <h4>Joining relations with Bloom Filter</h4>
Using bloom filter, bloom filter of size 2*10^6 bytes from server-1 to server-2. Then server-2 performs relation check with the passed bloom filter and sends back only those rows which have join attributes values in the bloom filter. In this case only 2*105*8*(length of each string).So totally data of size 2*10^6 + 2*105*8*(length of each string) bytes which is 3.6MB
<h4>Conclusion</h4>
We have saved communication cost of 16-3.6 = 12.4MB by using bloom filter.

<h3>Typical Applications </h3>

Web servers to store DNS entries </br>
Google big table's column lookups </br>
Spell check applicatons </br>
</p>
</body>
</html>
