## Organizing Domain Logic

The three (3) domain logic patterns are implemented in Java using the Revenue Recognition Problem as described in the PoEAA book by Martin Fowler.

- Transaction Script
- Domain Model
- Table Module

![Martin Folwer and PoEAA book cover](src/site/resources/images/Martin-Fowler-and-Patterns-of-Enterprise-Application-Architecture-book.jpg)

## The Revenue Recognition Problem

> *Taken from Martin Fowler's PoEAA book (page 112)*
>
> Revenue recognition is a common problem in business systems. It's all about when you can actually count the money you receive on your books. If I sell you a cup of coffee, it's a simple matter: I give you the coffee, I take your money, and I count the money to the books that nanosecond. For many things it gets complicated, however. Say you pay me a retainer to be available that year. Even if you pay me some ridiculous fee today, I may not be able to put it on my books right away because the service is to be performed over the course of a year. One approach might be to count only one-twelfth (1/12) of that fee for each month in the year, since you might pull out of the contract after a month when you realize that writing has atrophied my programming skills.
>
> The rules for revenue recognition are many, various, and volatile. Some are set by regulation, some by professional standards, and some by company policy. Revenue tracking ends up being quite a complex problem.
>
> I don't fancy delving into the complexity right now, so instead we'll imagine a company that sells three (3) kinds of products: word processors, databases, and spreadsheets. According to the rules, when you sign a contract for a word processor, you can book all the revenue right away. If it's a spreadsheet, you can book one-third (1/3) today, one-third in sixty (60) days, and one-third in ninety (90) days. If it's a database, you can book one-third today, one-third in thirty (30) days, and one-third in sixty (60) days. There's no basis for these rules other than my own fevered imagination. I'm told that the real rules are equally rational.
>
> *Emphasis, formatting, and numbers added for clarity*

