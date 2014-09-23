function omecdf( input )
% 1 - CDF
%   1 - cdf
    [f,x] = ecdf(input);
    f = (1-f)*100;
    plot(x,f);
end

