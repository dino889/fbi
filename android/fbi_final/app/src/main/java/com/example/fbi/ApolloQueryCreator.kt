package com.example.fbi

import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.toJson
import com.apollographql.apollo.exception.ApolloException
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

object ApolloQueryCreator{
    val apolloClient = ApolloClient.builder().serverUrl("http://27.113.21.252:6400/graphql").build()
    var result : String? = null
    //--------------------------------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------------//
    init {
        Logger.addLogAdapter(AndroidLogAdapter())
    }

    //로그 전송
    fun sendMutation(inputQuery: Queries, book_num: Int, user_ID : String, book_category : Int){
        if(inputQuery == Queries.addLog){
            apolloClient.mutate(AddLogMutation.Builder().book_num(book_num).user_ID(user_ID).book_category(book_category).build()
            ).enqueue(object : ApolloCall.Callback<AddLogMutation.Data>(){
                override fun onFailure(e: ApolloException) {

                }

                override fun onResponse(response: Response<AddLogMutation.Data>) {

                }

            })
        }
    }
    //회원가입 회원 정보 서버로 전송
    fun sendMutation(inputQuery: Queries, id: String, pw: String, name: String, age: Int, gender: String, category : Int){
        if(inputQuery == Queries.signUp){
            apolloClient.mutate(JoinUserMutation.Builder().user_ID(id).user_password(pw).user_name(name).user_age(age)
                .user_gender(gender).user_category(category).user_phone("1577").build()
            ).enqueue(object : ApolloCall.Callback<JoinUserMutation.Data>(){
                override fun onFailure(e: ApolloException) {
                    Logger.d(e)//실패하면 로그 확인해서 로그마다 출력 데이터 다르게하기
                }

                override fun onResponse(response: Response<JoinUserMutation.Data>) {//회원가입이 완료되었습니다
                    Logger.d("회원가입완료")
                }
            })
        }
    }
    //--------------------------------------------------------------------------------------------//
    //
    //--------------------------------------------------------------------------------------------//
    fun getQuery(inputQuery : Queries, queryCallback: (String) -> Unit) {

        if(inputQuery == Queries.mostSearchBook){
            // val apolloClient = ApolloClient.builder().serverUrl("http://27.113.21.252:6400").build()
            apolloClient.query(
                MostSearchbookQuery.Builder().build()
            ).enqueue(object  : ApolloCall.Callback<MostSearchbookQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    Logger.d(e.localizedMessage ?: "Error")
                }

                override fun onResponse(response: Response<MostSearchbookQuery.Data>) {
                    result = response.data?.toJson()
                    if(response.data != null){
                        result?.let { queryCallback(it) }
                    }
                }
            })
        }
        else if(inputQuery == Queries.getBookList){
            //val apolloClient = ApolloClient.builder().serverUrl("http://27.113.21.252:6400").build()
            apolloClient.query(
                GetBooklistQuery.Builder().build()
            ).enqueue(object  : ApolloCall.Callback<GetBooklistQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    Logger.d(e.localizedMessage ?: "Error")
                }

                override fun onResponse(response: Response<GetBooklistQuery.Data>) {
                    result = response.data?.toJson()
                    if(response.data != null){
                        result?.let { queryCallback(it) }
                    }
                }
            })
        }
        //----------------------------------------------------------------------------------------//
        //
        //----------------------------------------------------------------------------------------//
        // return result
    }

    fun getQuery( inputQuery : Queries, id : String, pw : String, queryCallback: (String) -> Unit){
        //----------------------------------------------------------------------------------------//
        //
        //----------------------------------------------------------------------------------------//

        if(inputQuery == Queries.loginUser){
            //val apolloClient = ApolloClient.builder().serverUrl("http://27.113.21.252:6400").build()
            apolloClient.newBuilder().build().query(
                LoginUserQuery.builder().id(id).pw(pw).build()
            ).enqueue(object : ApolloCall.Callback<LoginUserQuery.Data>(){
                override fun onFailure(e: ApolloException) {
                    Logger.d(e.localizedMessage ?: "Error")
                }
                override fun onResponse(response: Response<LoginUserQuery.Data>) {
                    result = response.data?.toJson()
                    if(response.data != null){
                        result?.let { queryCallback(it) }
                    }

                }
            })
        }
        //----------------------------------------------------------------------------------------//
        //
        //----------------------------------------------------------------------------------------//
//        return result
    }
    //--------------------------------------------------------------------------------------------//
    //
    //--------------------------------------------------------------------------------------------//

    fun getQuery(inputQuery : Queries, id : String,  queryCallback: (String) -> Unit) {

        if (inputQuery == Queries.searchedBook) {

            apolloClient.newBuilder().build().query(
                SearchedBookQuery.builder().id(id).build()
            ).enqueue(object : ApolloCall.Callback<SearchedBookQuery.Data>(){
                override fun onFailure(e: ApolloException) {
                    Logger.d(e.localizedMessage ?: "Error")
                }

                override fun onResponse(response: Response<SearchedBookQuery.Data>) {
                    Logger.d("검색했던 도서 목록 쿼리 요청")
                    result = response.data?.toJson()
                    if(response.data != null){
                        result?.let { queryCallback(it) }
                    }
                }
            })
        }
        //----------------------------------------------------------------------------------------//
        //
        //----------------------------------------------------------------------------------------//
        //유저정보(id)로 getBookmind
        if (inputQuery == Queries.getMindbook) {

            apolloClient.newBuilder().build().query(
                GetMindbookQuery.builder().id(id).build()
            ).enqueue(object : ApolloCall.Callback<GetMindbookQuery.Data>(){
                override fun onFailure(e: ApolloException) {
                    Logger.d(e.localizedMessage ?: "Error")
                }

                override fun onResponse(response: Response<GetMindbookQuery.Data>) {
                    Logger.d("getMindbook")
                    result = response.data?.toJson()
                    if(response.data != null){
                        result?.let { queryCallback(it) }
                    }
                }
            })
        }
    }

    fun getQuery( inputQuery : Queries, book_num : Int,  queryCallback: (String) -> Unit) {
        //----------------------------------------------------------------------------------------//
        //
        //----------------------------------------------------------------------------------------//

        if (inputQuery == Queries.getBookInfo) {

            apolloClient.newBuilder().build().query(
                GetBookInfoQuery.builder().num(book_num).build()
            ).enqueue(object : ApolloCall.Callback<GetBookInfoQuery.Data>(){
                override fun onFailure(e: ApolloException) {
                    Logger.d(e.localizedMessage ?: "Error")
                }

                override fun onResponse(response: Response<GetBookInfoQuery.Data>) {
                    Logger.d("탐색 도서 번호에 맞는 도서 정보 쿼리 요청")
                    result = response.data?.toJson()
                    Logger.d("탐색 도서 정보" + result)
                    if(response.data != null){
                        result?.let { queryCallback(it) }
                    }
                }
            })
        }
    }

    fun sendMutation(inputQuery: Queries, id: String, num: Int) {
        if(inputQuery == Queries.updateMindbook){

            apolloClient.mutate(UpdateMindbookMutation.Builder().user_ID(id).book_num(num).build()
            ).enqueue(object : ApolloCall.Callback<UpdateMindbookMutation.Data>(){
                override fun onFailure(e: ApolloException) {
                    Logger.d(e.localizedMessage ?: "Error")
                }

                override fun onResponse(response: Response<UpdateMindbookMutation.Data>) {
                    Logger.d("탐색 도서 저장 요청 후 응답")
                    result = response.data?.toJson()
                    Logger.d("저장된 탐색 도서 정보" + result)
                }
            })
        }
    }
}