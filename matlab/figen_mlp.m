load('data_mlp_ds_error.mat');

big_data_size = [];
big_errors = [];

n = length(data_size);

for i = 1 : n 
    if data_size(i) > 100
        big_data_size = [big_data_size data_size(i)];
        big_errors = [big_errors errors(i)];
    end
end