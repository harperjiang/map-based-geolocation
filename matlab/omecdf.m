function omecdf( input )
%PLOT_DENSITY Summary of this function goes here
%   1 - cdf
    [f,x] = ecdf(input);
    f = 1-f;
    plot(x,f);
end

