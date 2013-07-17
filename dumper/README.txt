README
======

Program for dumping raw geographic data from Slovak Cadastral portal into a 
text file.



Output format

The output contains a sequence of layers and for each layer all objects in this
layer. One line per layer and per object. For example

===Layer1
Object1
Object2
Object3
==Layer2
Object4
Object5 

Layer contains:
===Layer type|Layer name|Layer legend
Object contains:
Object type|Object id|Object name|Lengths of segments separated with ;|Vertices separated with ;

Possible object types are:
Point
Text
Polyline
Polygon

Vertices are points using S-JTSK geographic coordinate system. In future the
newer S-JTSK/03 geographic coordinate system will be used for Slovak cadastral
data.

Segments are sequences points. A polygon without hole has only one segment the
outer ring. A polygon with holes has one segment for the outer ring and more
segments for inner rings.
 