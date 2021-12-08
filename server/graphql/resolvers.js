import { getBooklist, getAllUser, getCategory,getLibrary,getLibraryBooks,getUserBook,getlog } from "./DB";
import {loginUser,joinUser,addLog,mostSearchbook,searchedBook,mindbook,updateMindbook,getUserbyID,updateUserPW,getMindbook,getByBookNum, findBookInShelf} from "./DB";

const resolvers = {
        Query: {
                        getBooklist: () => getBooklist(),
                        getAllUser: () => getAllUser(),
                        getUserBook: () => getUserBook(),
                        getCategory: () => getCategory(),
                        getLibrary: () => getLibrary(),
                        getLibraryBooks: () => getLibraryBooks(),
                        getlog:() => getlog(),
                        loginUser:(_,{id,pw}) => loginUser(id,pw),
                        mostSearchbook:() => mostSearchbook(),
                        mindbook:(_,{id}) => mindbook(id),
                        searchedBook:(_,{id}) => searchedBook(id),
                        getUserbyID:(_,{id}) => getUserbyID(id),
                        getMindbook:(_,{id}) => getMindbook(id),
                        getByBookNum:(_,{book_num}) =>getByBookNum(book_num),
                        findBookInShelf:(_,{book_num,user_ID}) => findBookInShelf(book_num,user_ID)
                        
                },
        Mutation:{
                        joinUser:(_,{user_ID,user_password,user_name,user_phone,user_age,user_gender}) => joinUser(user_ID,user_password,user_name,user_phone,user_age,user_gender),
                        addLog:(_,{book_num,user_ID,user_age,book_category}) => addLog(book_num,user_ID,user_age,book_category),
                        updateMindbook:(_,{user_ID,book_num}) => updateMindbook(user_ID,book_num),
                        updateUserPW:(_,{user_ID,book_num}) => updateUserPW(user_ID,user_password),
                        
        }
};

export default resolvers;