HashBaseTable

```
Data data = new Data(1, "A");
        Data data1 = new Data(2, "B");
        Data data2 = new Data(3, "C");
        Data data3 = new Data(4, "D");
        Data data4 = new Data(5, "E");
        Data data5 = new Data(6, "F");

        HashBasedTable<Long, String, Data> hashBasedTable = HashBasedTable.create();
        hashBasedTable.put(data.id, data.name,data );
        hashBasedTable.put(data1.id, data1.name,data1 );
        hashBasedTable.put(data2.id, data2.name,data2 );
        hashBasedTable.put(data3.id, data3.name,data3 );
        hashBasedTable.put(data4.id, data4.name,data4 );
        hashBasedTable.put(data5.id, data5.name,data5 );

        Map<Long, Data> dataMap = hashBasedTable.column("A");

        Set<Table.Cell<Long, String, Data>> cells = hashBasedTable.cellSet();

        System.out.println(Lists.newLinkedList(cells).get(0));

        System.out.println(dataMap);

        TreeBasedTable<Comparable, Comparable, Object> treeBasedTable = TreeBasedTable.create();
```





TreeBaseTable

ArrayTable

ImmutableTable