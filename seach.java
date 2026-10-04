class Solution {
    public static int searchInsert(int[] nums, int target) {
        for(int i=0;i<nums.length;i++){
            if(nums[i]==target){
                return i;
            }
        }
        return -1;
    }

    public static void main(String args[]){
        int nums[] ={1,3,5,6};
        int key = 2;
        int idx = searchInsert(nums,key);
        if(idx == -1){
            for(int i=0;i<nums.length;i++){
                int minPos =i;
                int maxPos = i+1;
                if(minPos<key && maxPos>key){
                    System.out.println("index is :"+(i+1));
                }
            }
        }
        else{
            System.out.println("key at index :"+idx);
        }

    }
}
