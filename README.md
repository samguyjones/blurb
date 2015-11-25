# blurb
This is a Java library to create random output.  It takes a set of
patterns from an XML data file, and it can produce random text using those
specifications.  It's inspired by a very old C program called "spew".

## Command Line Execution
If you have Maven (you need version 3), you
can build Blurb with "mvn install".  You can run the Jar created with
"java -jar Blurb.jar *datafile.xml*".  There's an additional option for
adding parameters.  You can add the option -p *param_name*=*param_value*
to add optional parameters.

## The Data File
The XML file spew uses defines a series of categories
and embeds.  Here is an example:

```
<blurb>
    <pattern>
        <entry><embed category="abstraction" filter="Ucfirst"/> is
        the root of <embed category="abstraction"/>.</entry> <entry
        weight="20">There is no <echo param="subject"/>, only <embed
        category="abstraction"/>.</entry>
    </pattern> <namespaces>
        <namespace name="main">
            <categories>
                <category name="abstraction">
                    <entry>love</entry> <entry>anger</entry>
                    <entry>happiness</entry> <entry>faith</entry>
                    <entry>despair</entry>
                </category>
            </categories>
        </namespace>
    </namespaces>
</blurb>
```

There are two major kinds of entries this data file uses:

##category
This includes "pattern" and "category".  Categories include
a list of entries.  When a category is called on, it picks one of its
entries at random and returns it.  "pattern" is the first category that's
called when you first run blurb.  All the other categories are called
"category" and are called by name.

The named categories all exist within a namespace, which exist to allow
multiple patterns with the same name without naming collisions.  If a
category is called without specifying a namespace, "main" is assumed.

###param
"Parameters" are values that can be defined either outside the data file
(on the command line or in a library that calls 'spew') or inside it within
a category.  If you specify a "param" for a category, you can set a "default"
attribute for the param, or you can create a "derive" element inside the
param.  If you set a "default" attribute, then the parameter defaults to that
value if it's not supplied sometime earlier in the execution.  The "derive"
element acts exactly like an 'entry' below in that it can have text, "embed",
"if", or "echo" in any order or combination.  This text executes and gets
set to the value of the parameter.

##entry
This is a possible result returned by a category.  When an entry
gets picked, the block of text gets formatted and returned.  Entry has
an attribute "weight", which determines how likely the entry is to
be picked.  If no weight is specified, the entry has a weight of one.
In addition to text, the following parameters can be embedded in an entry:

###embed
This is tells the entry to pull a value from the category named,
select an entry from that and return it.  It's possible for the embed to
pull from the category the entry itself belongs to.  Embed must have an
attribute "category" to name what category it will fetch from and can have
an attribute "namespace" if the category isn't in the "main" namespace.

Embed can also take "filter" as an attribute.  If so, the text that's
pulled from the category is run through a filter.  Right now, the only
filter is "Ucfirst".

###echo
If the blurb is called with a parameter, the echo attribute will
repeat the parameter.

###if
An "if" element is within an "embed" and specifies a block of content
that only displays if a parameter matches a given value.  An "if" element
needs two attributes, "param" to specify the name of a parameter and "match"
to specify a value that the parameter must match.

An "if" element also has a child element "then" which specifies a block of
content that renders if the parameter matches the "match" value.  An "if"
element *can* contain one or more "else" child elements.  An "else" child
element can contain a "param" and a "match" to act like another "if" statement
that can ony be hit if the first condition isn't met.  If an "else" statement
has no "param" or "match", it's executed any time the previous "if" (and any
other "else") statements aren't met.
