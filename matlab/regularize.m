function reg = regularize( x )
%REGULARIZE Summary of this function goes here
%   Detailed explanation goes here
[m,n] = size(x);
reg = zeros([m,n]);

for i = 1 : m
    minx = min(x(i,:));
    reg(i,:) = x(i,:) - minx;
end

end

