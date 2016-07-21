# Random Generator

*Project inspired by [Orteil's RandomGen](http://orteil.dashnet.org/randomgen/)*

A simple project I wrote when learning java. This is one of my first projects written in java and although the code is quite messy, I have found the project itself quite useful. I will possibly revisit this project someday but until then, I will keep it here.

## Overview

The Random Generator can be used to generate things like secure passwords,  dungeon loot, fake names, and more. The random generator works by picking an item randomly from a list defined in a text file or by using predefined utilities or ranges. Syntax details can be found in the documentation pdf file in this project.

## Examples

### Complex Password

```
!x(8-12,[[a-z]|!Abc([a-z])|[0-9]|_{%:30}|[@|#|$|%|^|&|*|\!]{%:60}])
```

Sample output:

```
_1K!ZCAtH4g
e&n3qm_eg^Z
e%MI96Nk
```

### "RPG" Style Names

```
#first_part
grim
gor
ano
hen
imp
nim
jac
kin
lob
mon
nop
oxen
pers
wad
win
rad
#middle_part
[a|e|i|o][b|c|d|f|g|h|j|k|l|m|p|r|s|t|v|w][|[middle_part]{%:20}]
#last_part
ah
ly
mia
fal
el
ex
ent
or
lorg
rod

#rpg_name
[first_part][middle_part][first_part]
[first_part][middle_part][last_part]
[first_part][last_part]
```

Sample output:

```
Gorel
Henly
Radod
Anofah
```

### Hex Color

```
\#!x(6,[!ABC([a-f])|[0-9]])
```

Sample output:
```
#A5F67D
#D2BEF2
#0C04C7
```
