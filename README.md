# blurb
This is a Java library to create random output.  It takes a set of patterns from an XML data file, and it can produce random
text using those specifications.  It's inspired by a very old C program called "spew".

## The Data File
The XML file spew uses defines a series of categories and embeds.  Here is an example:

```
<blurb>
    <pattern>
        <entry><embed category="abstraction" filter="Ucfirst"/> is the root of <embed category="abstraction"/>.</entry>
        <entry weight="20">There is no <echo param="subject"/>, only <embed category="abstraction"/>.</entry>
    </pattern>
    <namespaces>
        <namespace name="main">
            <categories>
                <category name="abstraction">
                    <entry>love</entry>
                    <entry>anger</entry>
                    <entry>happiness</entry>
                    <entry>faith</entry>
                    <entry>despair</entry>
                </category>
            </categories>
        </namespace>
    </namespaces>
</blurb>
```

There are two major kinds of entries this data file uses:

##category
This includes "pattern" and "category".  Categories include a list of entries.  When a category is called on, it picks one of
its entries at random and returns it.  "pattern" is the first category that's called when you first run blurb.  All the other
categories are called "category" and are called by name.

The named categories all exist within a namespace, which exist to allow multiple patterns with the same name without naming
collisions.  If a category is called without specifying a namespace, "main" is assumed.

##entry
This is a possible result returned by a category.  When an entry gets picked, the block of text gets formatted and returned.
Entry has an attribute "weight", which determines how likely the entry is to be picked.  If no weight is specified, the entry
has a weight of one.  In addition to text, the following parameters can be embedded in an entry:

###embed
This is tells the entry to pull a value from the category named, select an entry from that and return it.  It's possible for
the embed to pull from the category the entry itself belongs to.  Embed must have an attribute "category" to name what
category it will fetch from and can have an attribute "namespace" if the category isn't in the "main" namespace.

Embed can also take "filter" as an attribute.  If so, the text that's pulled from the category is run through a filter.  Right
now, the only filter is "Ucfirst".

###echo
If the blurb is called with a parameter, the echo attribute will repeat the parameter.  (TODO: Allow filter attribute on echo)

(TODO:  Describe "if" and parameters.)

