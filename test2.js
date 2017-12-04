a = e + f;
b = e*f + f;
c = e - f;
a = b + c * (a + b) + 1 && a || false;
c = a != b;