function [ result ] = calc_error( input )
    result = sqrt(input(1,:).^2 + input(2,:).^2);
    return;
end

