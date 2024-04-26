# query

---
A library for executing complex queries parsed and compiled using JavaCC.

Allows queries based on complex boolean logic and conditional expressions
formed from named properties.

The named properties may be derived from the columns of a database table, the
fields of a JSON object, or the attributes of an object in memory.

The library is designed to be extensible, allowing the addition of new data
sources and query types.

### Expressions
Expressions are formed using named properties and operators. The named
properties are referenced using dot-delimited notation.

#### Comparison
Comparison expressions are formed using the following operators:
- `eq` - equal to (`property eq 12`)
- `ne` - not equal to (`property ne 12`)
- `lt` - less than (`property lt 12`)
- `le` - less than or equal to (`property le 12`)
- `gt` - greater than (`property gt 12`)
- `ge` - greater than or equal to (`property ge 12`)

#### Boolean Logic
Boolean expressions are formed using the following operators (in order
of precedence):
- `AND` - logical AND (`A eq 1 AND B eq 2`)
- `OR` - logical OR (`A eq 1 OR B eq 2`)
- `NOT` - logical NOT (`NOT (A eq 1 OR B eq 2)`)

#### Precedence
The precedence of boolean operators can be controlled using parentheses.
By default, the AND operator has a higher precedence than the OR operator.

In the following example, the overall result can be `true` if either A is 1
and B is 2, or c is 3: 
```
A eq 1 AND B eq 2 OR C eq 3
```
However, by using parentheses, the precedence can be changed. Here the result
is `true` only if A is 1, and B is 2 or C is 3:
```
A eq 1 AND (B eq 2 OR C eq 3)
```

#### Compound Properties
Properties within a query are referenced using dot-delimited notation. For
example, given the following structure:
```
{
  "name": "John",
  "address": {
    "street": "123 Main St",
    "city": "Anytown",
    "state": "NY",
    "zip": "12345"
  }
}
```
The following query would match the object:
```
address.city eq 'Anytown'
```

#### Functions
Functions can be used to transform properties before comparison. The
following functions are supported:
- `lower` - convert the property value to lowercase (`lower(address.city) eq 'abc'`) 
- `upper` - convert the property value to uppercase (`upper(address.city) eq 'abc'`) 
- `contains` - true if property value contains given value (case-sensitive) (`contains(address.city, 'abc')`) 
- `endswith` - true if property value starts with given value (case-sensitive) (`endswith(address.city, 'abc')`) 
- `startswith` - true if property value ends with given value (case-sensitive) (`startswith(address.city, 'abc')`) 
- `isnull` -  true if the property value is null (`isnull(address.city)`) 
- `notnull` - true if the property value is NOT null (`notnull(address.city)`) 
