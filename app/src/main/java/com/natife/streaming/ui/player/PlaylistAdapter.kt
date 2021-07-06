package com.natife.streaming.ui.player

//class PlaylistAdapter(private val onClick:((Episode,List<Episode>)->Unit)): BaseListAdapter<Episode, PlaylistViewHolder>(PlaylistDiffUtils()) {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
//        return PlaylistViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_episode,parent,false))
//    }
//
//    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
//        super.onBindViewHolder(holder, position)
//        holder.itemView.setOnClickListener {
//            onClick.invoke(currentList[position],currentList)
//        }
//    }
//}
//class PlaylistDiffUtils: DiffUtil.ItemCallback<Episode>() {
//    override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
//        return false
//    }
//
//    override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
//        return false
//    }
//
//}