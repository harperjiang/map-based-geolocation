% Evaluate RB 

clear;
load('data_main.mat');
load('data_regions.mat');

[tm,tn] = size(input);


for nlp = 1:4
    region_errors = cell([tn,1]);
    data_size = zeros([tn,1]);
    for index = 1:tn
        data = input(:,index);
        center = regions{index,1};
        
        % Collect data around the center point
        round_input = regions{index,2};
        round_target = regions{index,3};
        
        [m,n] = size(round_input);
        
        data_size(index) = n;
        if n == 0
            continue;
        end
        [net,err] = train_rb(round_input, round_target,nlp*0.1);
        region_errors{index,1} = err;
    end
    save(strcat('data_localrb_',int2str(nlp),'.mat'),'region_errors','data_size');
end