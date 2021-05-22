WITH ['Amy','Bob','Cal','Dan','Eve'] AS names
UNWIND names AS name
CREATE (:Person {name: name})